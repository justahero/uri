URI Parser
==========

This Java library offers a way to parse URIs and supports the following RFCs

* [RFC 3986](http://www.ietf.org/rfc/rfc3987.txt) Uniform Resource Identifier (URI) Generic Syntax
* [RFC 3492](http://www.ietf.org/rfc/rfc3492.txt) Punycode, for encoding Internationalized Domain Names in Applications (IDNA)

It offers an easy way to build URIs (with Internationalized Domain Names)
and allows to parse URI strings and split them into their sub components. 


Motivation
----------

A few reasons for writing this library

* URI Class of Java 6 only conforms to [RFC 2396](http://www.ietf.org/rfc/rfc2396.txt) (Uniform Resource 
Identifies: Generic Syntax). it is superseded by [RFC 3986](http://www.ietf.org/rfc/rfc3986.txt)
* URLEncoder Class of Java 6 is not applicable for URIs, e.g. instead of `+` sign a space character
should be `%20`
* URI & URL have constructors with a lot of parameters but feel a bit unusable,
parameters have to be blank or null to omit them
* URI classes are available in different packages: `java.net`, `javax.xml.crypto`, `javax.xml.transform`,
`java.security`, `javax.print.attribute`, `javax.ws.rs.core.UriBuilder` (or at least their class names are a bit confusing)



Installation
------------

For now run [maven](http://maven.apache.org/) from the root folder. Copy the
resulting jar file (in target folder) to the desired location and reference it in your project.

    mvn package


Usage
-----


### Constructing a URI

Create an URI by chaining sub components together:

    URI uri = new URI().withScheme("http").withHost("www.example.com").withPath("/test.png");

which results in the URI string `http://www.example.com/test.png`.



### Parsing a URI

Create a URI simply by parsing a given string.

    URI uri = URI.parse("http://www.example.com")

If the string conforms to the generic URI layout the string is split into its
sub components.



#### Queries

    URI uri = new URI().withScheme("http").withHost("example.com").withQuery("page=2&per=10");

results in `http://example.com?page=2&per=10`

Even easier is to construct the query component by using the `addParam` method like:

    URI uri = new URI().withScheme("http").withHost("example.com").addParam("page", "2").addParam("per", "10");

that results in the same URI `http://example.com?page=2&per=10`.

It is also possible to sort the query parameters if required.

    URI uri = URI.parse("http://example.com/bar?value=12&temp=true").sortQuery();

would result in URI `http://example.com/bar?temp=true&value=12`.

To use a different delimiter character (default is `&`) to separate parameters in a query string, use the
`queryDelimiter` method and provide a character.

    URI uri = URI.parse("http://example.com/bar?q=test;page=10;per=20").queryDelimiter(';');

Sorting this URI would result in `http://example.com/bar?page=10;per=20;q=test`. It is advised to use
one of the de facto standard characters of the sub delimiters set in the query component (e.g. `&` or `;`).



Todos
-----

There are still a few things to do.

* Full [RFC 3490](http://www.ietf.org/rfc/rfc3490.txt) implementation
* Implementation of Template URI, see [RFC 6570](http://www.ietf.org/rfc/rfc6570.txt)
* Full [RFC 3987](http://www.ietf.org/rfc/rfc3987.txt) compatibility with Internationalized
Resource Identifier (Long term)
* Update to [RFC 5891](http://tools.ietf.org/html/rfc5891) for Internationalized 
Domain Names in Applications (IDNA), Protocol
* Better documentation of existing functionality
