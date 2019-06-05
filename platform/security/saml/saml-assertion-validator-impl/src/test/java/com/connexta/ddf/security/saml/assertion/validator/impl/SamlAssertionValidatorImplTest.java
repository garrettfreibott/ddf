/**
 * Copyright (c) Codice Foundation
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package com.connexta.ddf.security.saml.assertion.validator.impl;

import com.connexta.ddf.security.saml.assertion.validator.SamlAssertionValidator;
import ddf.security.assertion.saml.impl.SecurityAssertionSaml;
import ddf.security.http.SessionFactory;
import ddf.security.samlp.SimpleSign;
import ddf.security.samlp.SystemCrypto;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.wss4j.common.crypto.PasswordEncryptor;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.OpenSAMLUtil;
import org.apache.wss4j.common.util.DOM2Writer;
import org.codice.ddf.platform.filter.AuthenticationFailureException;
import org.codice.ddf.security.handler.api.SAMLAuthenticationToken;
import org.codice.ddf.security.util.SAMLUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.common.SignableSAMLObject;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml.saml2.core.impl.AssertionImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.mock;

public class SamlAssertionValidatorImplTest {

  public static Document readXml(InputStream is)
      throws SAXException, IOException, ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    dbf.setValidating(false);
    dbf.setIgnoringComments(false);
    dbf.setIgnoringElementContentWhitespace(true);
    dbf.setNamespaceAware(true);

    DocumentBuilder db = null;
    db = dbf.newDocumentBuilder();
    db.setEntityResolver(new DOMUtils.NullResolver());

    return db.parse(is);
  }

  public static final String LOCALHOST = "127.0.0.1";

  private SamlAssertionValidatorImpl validator;
  private SimpleSign simpleSign;

  // mocks
  @Mock private SessionFactory sessionFactory;

  @BeforeClass
  public static void init() {
    OpenSAMLUtil.initSamlEngine();
  }

  @Before
  public void setup() {

    MockitoAnnotations.initMocks(this);
    validator = new SamlAssertionValidatorImpl();
    validator.setSessionFactory(sessionFactory);
    validator.setSignatureProperties(
        getClass().getResource("/signature.properties").getPath());

    // system crypto
    PasswordEncryptor encryptionService = mock(PasswordEncryptor.class);
    SystemCrypto systemCrypto =
        new SystemCrypto("signature.properties", "signature.properties", encryptionService);
    simpleSign = new SimpleSign(systemCrypto);

  }

  @Test
  public void testGoodSaml() throws Exception {

    Element assertionElement = readDocument("/good_saml.xml").getDocumentElement();
    SignableSAMLObject signableSamlObject = (SignableSAMLObject) OpenSAMLUtil.fromDom(assertionElement);
    simpleSign.signSamlObject(signableSamlObject);

    String signedAssertionString = samlObjectToString(signableSamlObject);
    SecurityToken signedToken = SAMLUtils.getInstance().getSecurityTokenFromSAMLAssertion(signedAssertionString);

    SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
    principalCollection.add(new SecurityAssertionSaml(signedToken), "default");
    SAMLAuthenticationToken goodToken = new SAMLAuthenticationToken(null, principalCollection, LOCALHOST);
    validator.validate(goodToken);

  }

  @Test
  public void testExpiredSaml() throws Exception {

    Element thisToken = readDocument("/good_saml.xml").getDocumentElement();
    SecurityToken securityToken = new SecurityToken();
    securityToken.setToken(thisToken);
    //SAMLAuthenticationToken samlToken = new SAMLAuthenticationToken(null, securityToken, LOCALHOST);
    //validator.validate(samlToken);

  }

  @Test
  public void testSavedAuthenticationToken() {
  }

  private SAMLAuthenticationToken getToken(String location){
    String tokenString;

    try {
      tokenString = IOUtils.toString(getClass().getResourceAsStream(location), "UTF-8");
    } catch (IOException e) {
      Assert.fail("Unable to open resource file " + e);
      return null;
    }

    SecurityToken securityToken = SAMLUtils.getInstance().getSecurityTokenFromSAMLAssertion(tokenString);
    //return new SAMLAuthenticationToken(null, securityToken);
    return null;

  }

  private Document readDocument(String name)
      throws SAXException, IOException, ParserConfigurationException {
    InputStream inStream = getClass().getResourceAsStream(name);
    return readXml(inStream);
  }

  private String samlObjectToString(XMLObject samlObject) throws WSSecurityException {
    Document doc = DOMUtils.createDocument();
    doc.appendChild(doc.createElement("root"));

    Element samlElement = OpenSAMLUtil.toDom(samlObject, doc);
    return DOM2Writer.nodeToString(samlElement);
  }


}
