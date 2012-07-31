package com.uri;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

public class URITest {
    
    @Test
    public void canConstructURIFromSchemeAndHostName() throws URISyntaxException {
        URI uri = new URI().withScheme("http").withHost("example.com");
        Assert.assertEquals("http", uri.scheme());
        Assert.assertEquals("example.com", uri.host());
    }
    
    @Test
    public void canConstructURIWithScheme() throws URISyntaxException {
        URI uri = new URI().withScheme("ftp");
        Assert.assertEquals("ftp", uri.scheme());
    }
    
   /*
describe Addressable::URI, "when created from string components" do
  before do
    @uri = Addressable::URI.new(
      :scheme => "http", :host => "example.com"
    )
  end

  it "should have a site value of 'http://example.com'" do
    @uri.site.should == "http://example.com"
  end

  it "should be equal to the equivalent parsed URI" do
    @uri.should == Addressable::URI.parse("http://example.com")
  end

  it "should raise an error if invalid components omitted" do
    (lambda do
      @uri.omit(:bogus)
    end).should raise_error(ArgumentError)
    (lambda do
      @uri.omit(:scheme, :bogus, :path)
    end).should raise_error(ArgumentError)
  end
end

    */
    
    @Test
    public void hostWithPercentEncodedCharacters() throws URISyntaxException {
        URIAssert.host("http://www.%74%65%73%74.com", "www.test.com");
    }
    
    @Test
    public void hostWithSingleDigitPercentEncodedCharacter() {
        URIAssert.exception("http://www.ex%4.com");
    }
    
    @Test
    public void hostWithLetterInPercentEncodedOctet() {
        URIAssert.exception("http://www.ex%er%01.com");
    }
    
    @Test
    public void hostWithDoublePercentageSigns() {
        URIAssert.exception("http://www.tes%%t.com");
    }
    
    @Test
    public void hostTransformsToPercentEncodedLowerCaseCharacters() throws URISyntaxException {
        URIAssert.host("http://www.%66%6f%6f%62%61%72.com", "www.foobar.com");
    }
    
    @Test
    public void transformPercentEncodedUpperCaseCharacters() throws URISyntaxException {
        // => 'DBP'
        URIAssert.host("http://www.%44%42%50.com", "www.dbp.com");
    }
    
    @Test
    public void noTransformOfPercentEncodedSpecialCharacters() throws URISyntaxException {
        URIAssert.host("http://www.Test%7B%7D.com", "www.test%7B%7D.com");
    }
}
