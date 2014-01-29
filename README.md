# bacon - Linked Data Integration
This research prototype can be used for Linked Data Integration based on the [RDF Data Cube Vocabulary](http://www.w3.org/TR/vocab-data-cube/).

bacon is designed to work with cubes generated with the CODE DataExtractor but possibly works with external cubes. 
The snipped below shows, how the RDF Data Cube Vocabulary is used in this context.
Click on the image to see the full version.

[![Diagram: Cube Vocabulary](../../blob/master/cubeCrop.jpg?raw=true "Diagram: Cube Vocabulary")](../../blob/master/cube.jpg?raw=true)


## Features
In simple terms, bacon can merge two seperate RDF cubes into a single one. Hereby, schema- and data-integration are considered.

Additional features:
* Modifiy the structure definition of the RDF cube. (Add and merge components)
* Add metadata like provenance information (Using [PROV-O](http://www.w3.org/TR/prov-o/))
* Merge recommendations: Similarity functions for cubes
* SameAs inference for cube components (Using the [balloon service](https://github.com/schlegel/balloon))
* Label disambiguation (Using the disambiguation service developed within the [CODE-Project](http://code-research.eu/))

## License
[MIT License](../../blob/master/LICENSE)

## Building
Maven is used as the build managing tool.

---

The presented work was developed within the CODE project funded by the EU Seventh Framework Programme, grant agreement number 296150.
