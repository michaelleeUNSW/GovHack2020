# Generated by Django 2.2.5 on 2019-11-12 03:21

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('inventory', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='inventory',
            name='added_time',
            field=models.DateTimeField(default='2019-11-11', verbose_name='date added'),
            preserve_default=False,
        ),
    ]
