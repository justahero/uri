URI Parser
==========

This Java library offers a way to parse URIs and supports the following RFCs

* [RFC 3986](http://www.ietf.org/rfc/rfc3987.txt) Uniform Resource Identifier (URI) Generic Syntax
* [RFC 3490](http://www.ietf.org/rfc/rfc3490.txt) Internationalized Domain Names in Applications (IDNA)

It offers an easy way to build URIs (with Internationalized Domain Names)
and allows to parse URI strings and split them into their sub components. 


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


Todos
-----

* Full [RFC 3987](http://www.ietf.org/rfc/rfc3987.txt) compatibility with Internationalized
Resource Identifier (Long term)
* Better documentation of existing functionality