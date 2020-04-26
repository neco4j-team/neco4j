# neco4j #

This is an experimental Java 8 library for immutable collections and data structures. Note that the API is not stable yet, and that the collections might be quite inefficient. If you need production-ready immutable collections, I would suggest to use [vavr.io](vavr.io) instead.    

## Background ##

The basic idea of neco4j is to see all collections as "map-like" structures, with unique keys, and values stored under these keys. However, to make this work, the keys may act quite different from maps, e.g. more like "access points". E.g. for a double ended queue, the keys are just "FIRST" and "LAST". For lists, the keys will be the indices of the entries. In a set, we are only interested in the keys themselves, there are no useful values.

The starting point of this project was the observation how the Java API treats their maps very different from other collections, which didn't feel "right" for me. I hope this projects points towards a better abstraction for all kinds of collections.

## Findings ###

* It turned out that self-types are required
* The API of some collections got messy by specialization. However, this can't be avoided when we strive for a single abstraction
* Certain aspects (size limitation, sort order) are currently not considered, and are generally hard to integrate in the type hierarchy
* Having "dumb" collections, which can be used via type-classes, might be a more flexible and "correct" approach, and allow reusing code for "unrelated" classes (e.g. implement HashSet via HashMap)
* However, the type-class approach could turn out to be quite verbose (maybe a Kotlin implementation could avoid this)