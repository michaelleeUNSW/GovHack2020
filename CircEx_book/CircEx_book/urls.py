from rest_framework_swagger.renderers import SwaggerUIRenderer, OpenAPIRenderer
from rest_framework.schemas import get_schema_view
from django.contrib import admin
from django.urls import include, path
from rest_framework import routers
from api import views

router = routers.DefaultRouter()
# router.register(r'users', views.UserViewSet)
# router.register(r'groups', views.GroupViewSet)
# router.register(r'stores', views.stores_list, basename='stores')
schema_view = get_schema_view(title='API', renderer_classes=[
                              OpenAPIRenderer, SwaggerUIRenderer])

urlpatterns = [
    path('inventory/', include('inventory.urls')),
    path('admin/', admin.site.urls),
    path('api/', include(router.urls)),
    path('api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    path('docs/', schema_view, name='docs'),
    path("stores/<int:pk>", views.stores_list.as_view(), name="stores_list"),
    path("books/", views.books_list.as_view(), name="books_list"),
    path("stores/<int:store_id>/inventories/",
         views.inventories_list.as_view(), name=views.inventories_list.name),
    path("create_inventory/",
         views.create_inventory.as_view(), name=views.inventories_list.name)
]
