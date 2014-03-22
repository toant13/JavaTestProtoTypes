/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.server.internal.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.ext.ParamConverter;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.glassfish.jersey.internal.util.ReflectionHelper;
import org.glassfish.jersey.internal.util.collection.ClassTypePair;
import org.glassfish.jersey.server.internal.LocalizationMessages;
import org.glassfish.jersey.server.model.Parameter;

/**
 * Implementation of {@link MultivaluedParameterExtractorProvider}. For each
 * parameter, the implementation obtains a {@link ParamConverter param converter} instance via
 * {@link ParamConverterFactory} and creates the proper
 * {@link MultivaluedParameterExtractor multivalued parameter extractor}.
 *
 * @author Paul Sandoz
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
@Singleton
final class MultivaluedParameterExtractorFactory implements MultivaluedParameterExtractorProvider {

    private final ParamConverterFactory paramConverterFactory;

    /**
     * Create new multivalued map parameter extractor factory.
     *
     * @param stringReaderFactory string readers factory.
     */
    @Inject
    public MultivaluedParameterExtractorFactory(ParamConverterFactory stringReaderFactory) {
        this.paramConverterFactory = stringReaderFactory;
    }

    @Override
    public MultivaluedParameterExtractor<?> getWithoutDefaultValue(Parameter p) {
        return process(
                paramConverterFactory,
                null,
                p.getRawType(),
                p.getType(),
                p.getAnnotations(),
                p.getSourceName());
    }

    @Override
    public MultivaluedParameterExtractor<?> get(Parameter p) {
        return process(
                paramConverterFactory,
                p.getDefaultValue(),
                p.getRawType(),
                p.getType(),
                p.getAnnotations(),
                p.getSourceName());
    }

    @SuppressWarnings("unchecked")
    private MultivaluedParameterExtractor<?> process(
            ParamConverterFactory paramConverterFactory,
            String defaultValue,
            Class<?> rawType,
            Type type,
            Annotation[] annotations,
            String parameterName) {

        if (rawType == List.class || rawType == Set.class || rawType == SortedSet.class) {
            // Get the generic type of the list
            // If none default to String
            final List<ClassTypePair> ctps = ReflectionHelper.getTypeArgumentAndClass(type);
            ClassTypePair ctp = (ctps.size() == 1) ? ctps.get(0) : null;

            if (ctp == null || ctp.rawClass() == String.class) {
                return StringCollectionExtractor.getInstance(
                        rawType, parameterName, defaultValue);
            } else {
                final ParamConverter<?> converter = paramConverterFactory.getConverter(ctp.rawClass(), ctp.type(), annotations);
                if (converter == null) {
                    return null;
                }

                try {
                    return CollectionExtractor.getInstance(rawType, converter, parameterName, defaultValue);
                } catch (ExtractorException e) {
                    throw e;
                } catch (Exception e) {
                    throw new ProcessingException("Could not process parameter type " + rawType, e);
                }
            }
        }

        final ParamConverter<?> converter = paramConverterFactory.getConverter(rawType, type, annotations);
        if (converter != null) {
            try {
                return new SingleValueExtractor(converter, parameterName, defaultValue);
            } catch (ExtractorException e) {
                throw e;
            } catch (Exception e) {
                throw new ProcessingException("Could not process parameter type " + rawType, e);
            }
        }


        if (rawType == String.class) {
            return new SingleStringValueExtractor(parameterName, defaultValue);
        } else if (rawType.isPrimitive()) {
            // Convert primitive to wrapper class
            rawType = PrimitiveMapper.primitiveToClassMap.get(rawType);
            if (rawType == null) {
                // Primitive type not supported
                return null;
            }

            // Check for static valueOf(String )
            Method valueOf = AccessController.doPrivileged(ReflectionHelper.getValueOfStringMethodPA(rawType));
            if (valueOf != null) {
                try {
                    Object defaultDefaultValue = PrimitiveMapper.primitiveToDefaultValueMap.get(rawType);
                    return new PrimitiveValueOfExtractor(valueOf, parameterName, defaultValue, defaultDefaultValue);
                } catch (Exception e) {
                    throw new ProcessingException(LocalizationMessages.DEFAULT_COULD_NOT_PROCESS_METHOD(defaultValue, valueOf));
                }
            }

        }

        return null;
    }
}
