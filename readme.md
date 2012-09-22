URI
===

![Build Status](https://secure.travis-ci.org/justahero/uri.png?branch=master)

This Java library offers an intuitive way to construct and parse URIs. It aims to conform to the following RFCs.

* [RFC 3986](http://www.ietf.org/rfc/rfc3987.txt) Uniform Resource Identifier (URI) Generic Syntax
* [RFC 3492](http://www.ietf.org/rfc/rfc3492.txt) Punycode, for encoding Internationalized Domain Names in Applications (IDNA)


Installation
------------

For now run [maven](http://maven.apache.org/) from the root folder. Copy the resulting jar file (in target folder) to the desired location and reference it in your project.

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




Motivation
----------

There are few reasons describing the motivation for writing this library, mainly because ...

* URI handling should be instantly understandable
* simple construction of URI by only specifying the necessary components, it should not require empty or `null` parameters
* simple parsing of an URI and access to its sub-components (or often used aggregates, e.g. authority, request URI) should be easy
* the library aims to conform to [RFC 3986](http://www.ietf.org/rfc/rfc3986.txt) (Uniform Resource Identifier: Generic Syntax) and
therefore supersedes the implementation of Java API, which conforms to [RFC 2396](http://www.ietf.org/rfc/rfc2396.txt) (Java up to 1.6)
and [RFC 2732](http://www.ietf.org/rfc/rfc2732.txt) (Java 1.7)
* the Java URLEncoder class handles some characters differently then specified by RFC 3986
* Standard Java classes dealing with URI specific aspects are spread across different packages or at least their names are a bit confusing
* to learn a bit of the power of regular expressions
* ... it is fun!


