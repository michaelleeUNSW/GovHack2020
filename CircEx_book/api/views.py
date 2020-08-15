from .serializers import StoreSerializer, BookSerializer, InventorySerializer
from inventory.models import Store, Book, Inventory
from rest_framework import generics, status
from rest_framework.response import Response
from rest_framework.views import APIView
from django.shortcuts import render
# from django.contrib.auth.models import User, Group
# , UserSerializer, GroupSerializer
# from django.http import JsonResponse
# from django.shortcuts import get_object_or_404


class stores_list(generics.RetrieveUpdateDestroyAPIView):
    queryset = Store.objects.all()
    serializer_class = StoreSerializer


class books_list(generics.ListCreateAPIView):
    queryset = Book.objects.all()
    serializer_class = BookSerializer


class inventories_list(generics.ListCreateAPIView):
    name = 'inventories_list'

    def get_queryset(self):
        queryset = Inventory.objects.filter(store_id=self.kwargs['store_id'])
        return queryset
    serializer_class = InventorySerializer


class create_inventory(APIView):
    name = 'create_inventory'

    def post(self, request, book_id, store_id):
        # request与url中取出数据，并序列化
        voted_by = request.data.get('voted_by')
        data = {'book_id': book_id, 'store_id': store_id}
        serialized = InventorySerializer(data=data)
        return Response(serialized.data, status=status.HTTP_201_CREATED)
        # 验证数据有效性（必要），保存到模型（表）；并返回状态
        # if serialized.is_valid():
        #     serialized.save()
        #     return Response(serialized.data, status=status.HTTP_201_CREATED)
        # else:
        #     # 返回400状态
        #     return Response(serialized.errors, status=status.HTTP_400_BAD_REQUEST)

# class UserViewSet(viewsets.ModelViewSet):
#     queryset = User.objects.all().order_by('-date_joined')
#     serializer_class = UserSerializer


# class GroupViewSet(viewsets.ModelViewSet):
#     """
#         retrieve:
#             Return a group instance.

#         list:
#             Return all groups, ordered by most recently joined.

#         create:
#             Create a new group.

#         delete:
#             Remove an existing group.

#         partial_update:
#             Update one or more fields on an existing group.

#         update:
#             Update a group.
#     """
#     queryset = Group.objects.all()
#     serializer_class = GroupSerializer
