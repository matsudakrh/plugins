// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.videoplayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import io.flutter.plugin.common.EventChannel;
import io.flutter.view.TextureRegistry;
import java.util.Map;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class VideoPlayerTest {
  // This is only a placeholder test and doesn't actually initialize the plugin.
  @Test
  public void initPluginDoesNotThrow() {
    final VideoPlayerPlugin plugin = new VideoPlayerPlugin();
  }

  @Test
  public void videoPlayerSendInitializedSetsRotationCorrectionForRotationDegrees180() {
    Format format = new Format.Builder().setRotationDegrees(180).build();
    SimpleExoPlayer mockExoPlayer = mock(SimpleExoPlayer.class);
    when(mockExoPlayer.getVideoFormat()).thenReturn(format);
    final VideoPlayer player = new VideoPlayer(
            mock(EventChannel.class),
            mock(TextureRegistry.SurfaceTextureEntry.class),
            mock(VideoPlayerOptions.class),
            mockExoPlayer
    );
    QueuingEventSink mockEventSink = mock(QueuingEventSink.class);
    player.eventSink = mockEventSink;
    player.isInitialized = true;

    player.sendInitialized();

    ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
    verify(mockEventSink).success(eventCaptor.capture());
    @SuppressWarnings("unchecked") Map<String, Object> capturedEventMap =
            (Map<String, Object>) eventCaptor.getValue();
    assertEquals(Math.PI, capturedEventMap.get("rotationCorrection"));
  }

  @Test
  public void videoPlayerSendInitializedDoesNotSetRotationCorrectionForRotationDegreesNot180() {
    Format format = new Format.Builder().setRotationDegrees(90).build();
    SimpleExoPlayer mockExoPlayer = mock(SimpleExoPlayer.class);
    when(mockExoPlayer.getVideoFormat()).thenReturn(format);
    final VideoPlayer player = new VideoPlayer(
            mock(EventChannel.class),
            mock(TextureRegistry.SurfaceTextureEntry.class),
            mock(VideoPlayerOptions.class),
            mockExoPlayer
    );
    QueuingEventSink mockEventSink = mock(QueuingEventSink.class);
    player.eventSink = mockEventSink;
    player.isInitialized = true;

    player.sendInitialized();

    ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
    verify(mockEventSink).success(eventCaptor.capture());
    @SuppressWarnings("unchecked") Map<String, Object> capturedEventMap =
            (Map<String, Object>) eventCaptor.getValue();
    assertFalse(capturedEventMap.containsKey("rotationCorrection"));
  }
}
