IDNA
====

# [RFC 3490](http://www.ietf.org/rfc/rfc3490.txt)

Used to represent **Internationalized Domain Names** (IDN) and is targetd to named hosts only.
Uses the Unicode character set.

It desribes two operations to convert a IDN.

* `toASCII` is a function to convert a IDN to a representation consisting of only US-ASCII characters.
* `toUnicode` is a function used when displaying names to users

The `toASCII` function can fail. IDNA requires that implementations process input strings
with **Nameprep** and then with **Punycode**. An IDNA implementation must fully implement
Nameprep and Punycode.
The `toASCII` generates a ACE label, where ACE means ASCII Compatible Encoding. An ACE label
is an Internationalized label that can be rendered in ASCII. It is equivalent to an Internationalized
label that cannot be rendered in ASCII. ACE label are unsuitable for display to users.
To display an Internationalized label the `toUnicode` function is used, that converts any label
to an equivalent non-ACE label (that uses the Unicode character set). Every ACE label begins with
the ACE prefix specified in section 5.


### 3.1 Requirements

Whenever dots are used as label separators, the following characters MUST be recognized as dots

    U+002E, U+3002, U+FF0E, U+FF61

Before using an IDN in an IDN-unaware domain name slot, use `toASCII`method. Label separator dots
are changed to `U+002E`.

ACE labels should be shown as IDN if the environment can handle it. Use the `toUnicode` function
to achieve a non-ACE label, applied to each separate label.

When to IDNs are compared (all labels of the domain name) they MUST be considered to match
if and only if they are equivalent. Both IDNs must match their ACE form.


### 4 Conversion Operations

* First, decide where the domain is a "stored string" or "query string" If the conversion follows
the "queries" rule from [Preparation of Internationalied Strings](http://www.ietf.org/rfc/rfc3454.txt).
* Split the domain name into separate labels.
* For each label decide whether or not to enforce restrictions on ASCII characters in host names, see
[Requirements for Internet Hosts](http://www.ietf.org/rfc/rfc1122.txt). A flag `UseSTD3ASCIIRules"
can be set to enforce conversion.
* Process each label with either `toASCII` or `toUnicode`.
* If `toASCII` function was applied, use label separator `U+002E`.


#### 4.1 toASCII

`toASCII` takes a sequence of Unicode characters and transforms it into a sequence of ASCII characters
(0..7F). The function can fail. The inputs to `toASCII` is a sequence of code points, and the
flags `AllowUnassigned` and `UseSTD3ASCIIRules`. `toASCII` does not alter characters that are already
ASCII characters. Applying the function multiple times is the same as applying it once (idempotent).

The following steps are executed

1. If the sequence contains any characters outside ASCII range (0..7E) proceed step 2
2. Perform the steps specified in [Nameprep](http://www.ietf.org/rfc/rfc3491.txt). The
flag `AllowUnassigned` is used in the method.
3. If the `UseSTD3ASCIIRules` flag is set the perform the following checks
    1. Verify the absence of these code points: `0..2C, 2E..2F, 3A..40, 5B..60, 7B..7F`
    2. Verify the absence of `U+002D` as leading or trailing character of the sequence
4. If the sequence contains any code points outside ASCII (0..7F) proceed in step 5, or else step 8
5. Verify that the sequence does not begin with the ACE prefix
6. Encode the sequence using the encoding algorithm in [Punycode](http://www.ietf.org/rfc/rfc3492.txt).
7. Prepend the ACE prefix.
8. Verify that the number of code points is in the range 1 to 63 (inclusive)


#### 4.2 toUnicode

`toUnicode` takes a sequence of Unicode code points and returns a sequence of Unicde code points.
If the input sequence is in ACE form an equivalent Internalionalized label is returned.
The `toUnicode` never fails. If any step fails the original input sequence is returned.

The following steps are executed

1. If all code points in the sequence are ASCII characters skip to step 3
2. Perform the steps specified in [Nameprep](http://www.ietf.org/rfc/rfc3491.txt) and fail
if there is an error. The `AllowUnassigned` flag is used in Nameprep.
3. Verify that the sequence begins with the ACE prefix
4. Remove the ACE prefix
5. Decode the sequence using the decoding algorithm in [Punycode](http://www.ietf.org/rfc/rfc3492.txt)
and fail if there is an error.
6. Apply toASCII function
7. Verify that the result of step 6 matches the copy from step 3.
8. Return the saved copy from step 5


### ACE prefix

The ACE prefix used in conversion operations is two alpha-numeric ASCII characters plus two
hyphen-minuses. `toASCII` and `toUnicode` must recognize the ACE prefix in a case-insensitive manner.

The ACE prefix for IDNA is `xn--`.

An ACE label might be `xn--de-jg4avhby1noc0d` where `de-jg4avhby1noc0d` is the part of Punycode method.



