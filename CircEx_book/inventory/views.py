from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse, Http404
from .models import Book


def index(request):
    book_list = Book.objects.all()
    context = {'latest_book_list': book_list}
    return render(request, 'inventory/index.html', context)


def detail(request, book_id):
    book = get_object_or_404(Book, pk=book_id)
    return render(request, 'inventory/detail.html', {'book': book})
