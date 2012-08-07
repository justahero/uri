URI Parser
==========

This Java library offers a way to parse URIs according to RFC 3986.

It offers an easy way to build URIs and allows to parse URI strings and split
them into their sub components. 



Installation
------------

For now run [maven](http://maven.apache.org/) from the root folder. Copy the
resulting jar file (in target folder) to the desired location and reference it in your project.

    mvn package


Usage
-----

### Constructing URI

Create an URI by chaining sub components together:

    URI uri = new URI().withScheme("http").withHost("www.example.com").withPath("/test.png");

which results in the URI string `http://www.example.com/test.png`.


### Parsing URI

Create a URI simply by parsing a given string.

    URI uri = URI.parse("http://www.example.com")

If the string conforms to the generic URI layout the string is split into its
sub components.
