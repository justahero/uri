URI Encoder
===========

The URI Encoder is based on the [RFC 3986](http://www.ietf.org/rfc/rfc3986.txt).

The specification uses the Augmented Backus-Naur Form (ABNF) (see [RFC 2234](http://www.ietf.org/rfc/rfc2234.txt)) for
Syntax Specification (see section 1.3 of RFC 3986)

    ALPHA - [a-zA-Z]
    CR - carriage return
    DIGIT - [0 - 9]
    DQUOTE - double quotes "
    HEXDIG - [0 - 9a - f]
    LF - line feed
    SP - space


### Percent-Encoding (2.1)

An 'octet' is a 8 bit character. Percent encoding is used to represent a data octet in a component when the corresponding character is outside the allowed set

    percent encoded char = '%' + HEXDIG + HEXDIG

example:

    ' ' => '%20'

**Note** Hexadecimal values 'A' to 'F' should be in uppercase.



### Reserved Characters (2.2)

generic delimiters

    ":" / "/" / "?" / "#" / "[" / "]" / "@"

subcomponent delimiters

    "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="

Unreseved characters (2.3)

    [a - zA - Z] [0 - 9] '-' '.' '_' '~'

Reserved character include all generic and subcomponent delimiters.
For better URI comparisons, percent encoded ASCII octets that are unreserved characters
should be decoded to their single character presence. Normalizing percent encoded unreserved
characters is therefore suggested!



### Encoding / Decoding (2.4)

Typically percent encoding is only applied when generating a URI from different components (scheme, host, path etc)

URI Syntax

    URI = scheme ":" hier-part [ "?" query ] [ "#" fragment ]

    hier-part = "//" authority path-abempty
                 / path-absolute
                 / path-rootless
                 / path-empty

* when authority present, path must be empty or start with '/'
* when authority not present, path cannot begin with '//'

Example:

    foo://example.com:1234/path/to?param=value#top

Components:

    foot               # scheme
    example.com:1234   # authority
    /path/to           # path
    param=value        # query
    top                # fragment

* scheme and path are required
* path can be empty (no characters)



#### Scheme (3.1)

    Scheme = ALPHA *( ALPHA || DIGIT || '+' || '-' || '.' )
    [a-zA-Z]+[a-zA-Z0-9+.-]*

#### Authority (3.2)

    Authority = [ userinfo "@" ] host [ ":" port ]
    User Info


References
----------

### Specifications

* [RFC 3986](http://www.ietf.org/rfc/rfc3986.txt) - Uniform Resource Identifier (URI): Generic Syntax
* [RFC 2718](http://www.ietf.org/rfc/rfc2718.txt) - Guidelines for new URL Schemes


### Discussions

A few discussions on URL encoding in Java

* http://www.subbu.org/blog/2008/02/uri-escaping-and-javaneturlencoder
* http://stackoverflow.com/questions/5913623/rfc3986-which-pchars-need-to-be-percent-encoded
* http://greenbytes.de/tech/webdav/rfc3986.html#rfc.section.2.1
* http://www.ietf.org/rfc/rfc3986.txt
* http://javadoc.google-api-java-client.googlecode.com/hg/1.0.10-alpha/index.html


### Links / Articles

* [Addressable](https://github.com/sporkmonger/addressable) Ruby gem, based on RFC 3986
* [URI Validation / Parsing](http://snipplr.com/view/6889/) - Regular Expressions
* [Google API Java Client API](http://javadoc.google-api-java-client.googlecode.com/hg/1.0.10-alpha/index.html)
