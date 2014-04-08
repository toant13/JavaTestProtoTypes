from django.conf.urls import patterns, include, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('simplepastebin.views',
    url(r'^snippets/$', 'snippet_list'),
    url(r'^snippets/(?P<pk>[0-9]+)/$', 'snippet_detail'),
    # Examples:
    # url(r'^$', 'RestApp.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),

)
