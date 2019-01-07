/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.rs.security.httpsignature;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.cxf.rs.security.httpsignature.utils.DefaultSignatureConstants;
import org.apache.cxf.rs.security.httpsignature.utils.SignatureHeaderUtils;

public class MessageSigner {
    private final String digestAlgorithmName;
    private SignatureCreator signatureCreator;

    public MessageSigner(String signatureAlgorithmName,
                         String digestAlgorithmName,
                         PrivateKey privateKey,
                         String keyId) {
        this.digestAlgorithmName = digestAlgorithmName;
        this.signatureCreator = new TomitribeSignatureCreator(signatureAlgorithmName, privateKey, keyId);
    }

    public MessageSigner(PrivateKey privateKey,
                         String keyId) {
        this(DefaultSignatureConstants.SIGNING_ALGORITHM,
                DefaultSignatureConstants.DIGEST_ALGORITHM,
                privateKey,
                keyId);
    }

    public void sign(Map<String, List<String>> messageHeaders,
                     String uri,
                     String method) throws IOException {
        inspectArguments(messageHeaders, uri, method);

        messageHeaders.put("Signature", Collections.singletonList(signatureCreator.createSignature(messageHeaders,
                uri,
                method
        )));
    }

    public void sign(Map<String, List<String>> messageHeaders,
                     String uri,
                     String method,
                     String messageBody) throws NoSuchAlgorithmException, IOException {
        inspectArguments(messageHeaders, uri, method);

        Objects.requireNonNull(messageBody);

        messageHeaders.put("Digest",
                Collections.singletonList(SignatureHeaderUtils
                        .createDigestHeader(messageBody, digestAlgorithmName)));

        messageHeaders.put("Signature",
                Collections.singletonList(signatureCreator.createSignature(messageHeaders,
                        uri,
                        method
        )));
    }

    private void inspectArguments(Map<String, List<String>> messageHeaders,
                                  String uri,
                                  String method) {
        Objects.requireNonNull(messageHeaders);
        Objects.requireNonNull(uri);
        Objects.requireNonNull(method);

        if (messageHeaders.isEmpty()) {
            throw new IllegalStateException("message headers are empty");
        }
        messageHeaders.forEach((key, list) -> Objects.requireNonNull(list));
        messageHeaders.forEach((key, list) -> list.forEach(Objects::requireNonNull));
    }
}
