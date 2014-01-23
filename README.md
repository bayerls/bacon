# bacon - Linked Data Integration

This research prototype can be used for Linked Data Integration based on the [RDF Data Cube Vocabulary](http://www.w3.org/TR/vocab-data-cube/).

## Features

In simple terms, bacon can merge two seperate RDF cubes into a single one. Hereby, schema- and data-integration are considered.

Additional features:

* Modifiy the structure definition of the RDF cube. (Add and merge components)
* Add metadata like provenance information (Using [PROV-O](http://www.w3.org/TR/prov-o/))
* Merge recommendations: Similarity functions for cubes
* SameAs inference for cube components (Using the [balloon service](https://github.com/schlegel/balloon))
* Label disambiguation (Using the disambiguation service developed within the [CODE-Project](http://code-research.eu/))



## License
MIT License

## Building
Maven is used as the build managing tool.

---

The presented work was developed within the CODE project funded by the EU Seventh Framework Programme, grant agreement number 296150.
