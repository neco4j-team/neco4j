# neco4j #

This is an experimental Java 8 library for immutable collections and data structures. Note that the API is not stable yet, and that the collections might be quite inefficient. If you need production-ready immutable collections, I would suggest to use [vavr.io](vavr.io) instead.    

## Background ##
The basic idea of neco4j is to see all collections as "map-like" structures, with unique keys, and values stored under these keys. However, to make this work, the keys may act quite different from maps, e.g. more like "access points". E.g. for a deque (double ended queue), the keys would be just "front" and "back". For lists, the keys will be the indices of the entries. In a set, we are only interested in the keys themselves, there are no useful values.

The starting point of this project was the observation how the Java API treats their maps very different from other collections, which didn't feel "right" for me. I hope this projects points towards a better abstraction for all kinds of collections. 