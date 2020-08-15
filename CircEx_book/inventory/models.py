import datetime
from django.db import models
from django.utils import timezone


class Book(models.Model):
    def __str__(self):
        return self.title

    isbn = models.IntegerField(default=0)
    title = models.CharField(max_length=200)
    edition = models.IntegerField(default=1)
    author = models.CharField(max_length=200)
    classification = models.CharField(max_length=200)
    language = models.CharField(max_length=200)
    age_group = models.CharField(max_length=200)
    genre = models.CharField(max_length=200)


class Store(models.Model):
    def __str__(self):
        return self.name

    name = models.CharField(max_length=200)
    location = models.CharField(max_length=200)
    coordinate = models.CharField(max_length=200)
    phone = models.CharField(max_length=200)
    opening_hour = models.CharField(max_length=200)


class Inventory(models.Model):
    def was_added_recently(self):
        return self.added_time >= timezone.now() - datetime.timedelta(days=30)

    book_id = models.ForeignKey(Book, on_delete=models.CASCADE)
    store_id = models.ForeignKey(Store, on_delete=models.CASCADE)
    quality = models.CharField(max_length=200)
    price = models.FloatField()
    image = models.CharField(max_length=200)
    added_time = models.DateTimeField('date added')
