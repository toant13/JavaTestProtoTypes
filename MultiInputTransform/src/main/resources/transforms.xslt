<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:foo="http://whatever">
  <xsl:output method="xml" indent="yes"/>
  

  
  <!-- 
  <xsl:param name="fileName"/>
  <xsl:param name="updates" select="document($fileName)" />
   -->
  <xsl:param name="updates" />
  <xsl:variable name="updateItems" select="$updates/feed/entry" />
  
  <xsl:param name="list" />
   
  <xsl:function name="foo:compareCI">
    <xsl:param name="string1"/>
    <xsl:if test="$string1='file2'">
    	<xsl:value-of select="$string1"/>
    </xsl:if>
  </xsl:function>
   
  
  <xsl:template match="@* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="feed">
  	<xsl:for-each select="$list">
  		
  		<func>
  			<xsl:attribute name="fakefunc">
  				<xsl:variable name="now" select="." />
  				<xsl:value-of select="foo:compareCI('$now')"/>
  			</xsl:attribute>
  		</func>
  		<number>
  			<xsl:attribute name="filename"><xsl:value-of select="."/></xsl:attribute>
  		
  		</number>
  	</xsl:for-each>

    <xsl:copy>
      <xsl:apply-templates select="@* | node()[not(self::entry)] | 
                                   entry[not(id = $updateItems/id)]" />
      <xsl:apply-templates select="$updateItems" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>