from django.contrib import admin

from .models import Book, Store, Inventory

admin.site.register([Book, Store, Inventory])
