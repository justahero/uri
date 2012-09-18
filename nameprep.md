Stringprep
==========

# [RFC 3454](http://www.ietf.org/rfc/rfc3454.txt)

### 2. Preparation Overview

* Map, for each character in the input sequence check if there exists a mapping and if so
replace it with its mapping
* Normalize the result of step 1 using Unicode normalization
* Prohibit characters that are not allowed in the output. If there is one found return with error
* Check bidirectional strings, check for right-to-left characters, and if they are found
ensure that the whole string satisfies the requirements for bidi strings. If not return an error.



### 3. Mapping

Each character of the input sequence must be checked if there exists a mapping. For any individual
character the mapping table may specify that a character be mapped to nothing, or mapped to one
other character, or mapped to a string of other characters.
Mapped characters are not rescanned in the mapping table.


#### 3.1. Commonly mapped to nothing

The following characters are simply deleted from the input (they are mapped to nothing).
See Table B.1 in Appendix


#### 3.2 Case folding

If a profile maps characters for case-insensitive comparison that profile should use
appendix B.2 if it also uses Unicode normalization form KC, and the profile should use
appendix B.3 if it does not use Unicde normalization. These tables map from uppercase to
lowercase characters.

If the profile is using Unicde normalization form KC there are some characters that do not
have mappings but still need processing. These characters can be determmined by the following
algorithm.

For a single character `a`

    b = NormalizeWithKC(Fold(a))
    c = NormalizeWithKC(Fold(b))

If `c` is not the same a `b` add a mapping for **`a` to `c`**


### 4. Normalization

A profile can specify two allowed Unicode normalizations.

* no Normalization
* Unicode Normalization with form KC as according to [Unicode Standard Annex 15](http://www.unicode.org/reports/tr15/tr15-22.html)
* A third form of normalization exists (Unicode normalization with form C) but is not used in Stringprep.


### 5. Prohibited Output

The character sequence must be checked for prohibited characters (code points). See appendix C
for a complete list of prohibited characters. The mapping table in appendix C must be used by
implementations.




Nameprep
========

# [RFC 3491](http://www.ietf.org/rfc/rfc3491.txt)

Describes how to prepare Internationalized Domain Names (IDN) labels.
**Nameprep** is a specific propfile of [Stringprep](http://www.ietf.org/rfc/rfc3454.txt)
for use with IDN.

It defines the following:

* Intended applicability of this profile: Internationalized Domain Names (IDN)
* Character repertoire of input and output to Stringprep: Unicode 3.2 (section 2)
* Mappings used (section 3)
* Unicde normalization used (section 4)
* Characters that are prohibited as output (section 5)
* Bidirectional character handling (section 6)


### 2. Character Repertoire

The **Nameprep** profiles uses Unicode 3.2 as specified in Stringprep Appendix A.


### 3. Mapping

This profile specifies mapping using the following tables from Stringprep.

* Table B.1 and B.2


### 4. Normalization

This profile specifies using Unicode normalization form KC as described in Stringprep.


### 5. Prohibited Output

This profile specifies prohibiting using the following tables from Stringprep.

* Table C.1.2 and C.2.2
* Table C.3, C4, C.5, C.6, C.7, C.8, C.9


### 6. Bidirectional Characters

This profile specifies checking bidirectional strings as described in Stringprep (section 6)


### 7. Unassigned code points in IDN

If the processing specifies that a list of unassigned code points be used, the system
uses table A.1 from Stringprep as its list of unassigned code points.