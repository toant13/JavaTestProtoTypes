'''
Created on Apr 4, 2014

@author: toantran
'''
from books.models import Author
from rest_framework import serializers


class AuthorSerializer(serializers.ModelSerializer):
    """
    Serializing all the Authors
    """
    class Meta:
        model = Author
        fields = ('id', 'first_name', 'last_name')