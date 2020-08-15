from django.urls import path

from . import views

app_name = 'inventory'
urlpatterns = [
    path('', views.index, name='index'),
    path('<int:book_id>', views.detail, name='detail'),
]
