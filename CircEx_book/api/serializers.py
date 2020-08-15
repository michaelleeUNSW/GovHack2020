from django.contrib.auth.models import User, Group
from inventory.models import Store, Book, Inventory
from rest_framework import serializers


# class UserSerializer(serializers.HyperlinkedModelSerializer):
#     class Meta:
#         model = User
#         fields = ('url', 'username', 'email', 'groups')


# class GroupSerializer(serializers.HyperlinkedModelSerializer):
#     class Meta:
#         model = Group
#         fields = ('url', 'name')


class StoreSerializer(serializers.ModelSerializer):
    class Meta:
        model = Store
        # fields = ('url', 'name', 'location','coordinate', 'phone', 'opening_hour')
        fields = '__all__'


class BookSerializer(serializers.ModelSerializer):
    class Meta:
        model = Book
        fields = '__all__'


class InventorySerializer(serializers.ModelSerializer):
    book_id = BookSerializer()
    store_id = StoreSerializer()

    class Meta:
        model = Inventory
        fields = '__all__'
