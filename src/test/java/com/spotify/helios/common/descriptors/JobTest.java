/**
 * Copyright (C) 2013 Spotify AB
 */

package com.spotify.helios.common.descriptors;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.BaseEncoding;

import com.spotify.helios.common.Hash;
import com.spotify.helios.common.Json;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class JobTest {

  @Test
  public void verifySha1ID() throws IOException {
    final Map<String, Object> expectedConfig = ImmutableMap.of("command", asList("foo", "bar"),
                                                               "image", "testStartStop:4711",
                                                               "name", "foozbarz",
                                                               "version", "17",
                                                               "env", new HashMap<>());

    final String expectedInput = "foozbarz:17:" + hex(Json.sha1digest(expectedConfig));
    final String expectedDigest = hex(Hash.sha1digest(expectedInput.getBytes(UTF_8)));
    final JobId expectedId = JobId.fromString("foozbarz:17:" + expectedDigest);

    final Job descriptor = Job.newBuilder()
        .setCommand(asList("foo", "bar"))
        .setImage("testStartStop:4711")
        .setName("foozbarz")
        .setVersion("17")
        .build();

    assertEquals(expectedId, descriptor.getId());
  }

  @Test
  public void verifySha1IDWithEnv() throws IOException {
    final Map<String, String> env = ImmutableMap.of("FOO", "BAR");
    final Map<String, Object> expectedConfig = ImmutableMap.of("command", asList("foo", "bar"),
                                                               "image", "testStartStop:4711",
                                                               "name", "foozbarz",
                                                               "version", "17",
                                                               "env", env);

    final String expectedInput = "foozbarz:17:" + hex(Json.sha1digest(expectedConfig));
    final String expectedDigest = hex(Hash.sha1digest(expectedInput.getBytes(UTF_8)));
    final JobId expectedId = JobId.fromString("foozbarz:17:" + expectedDigest);

    final Job descriptor = Job.newBuilder()
        .setCommand(asList("foo", "bar"))
        .setImage("testStartStop:4711")
        .setName("foozbarz")
        .setVersion("17")
        .setEnv(env)
        .build();

    assertEquals(expectedId, descriptor.getId());
  }

  private String hex(final byte[] bytes) {
    return BaseEncoding.base16().lowerCase().encode(bytes);
  }
}
