
package com.caloriecounter.calorie.ui.livewallpaper.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.util.List;

public class GLWallpaperService extends WallpaperService {
    @SuppressWarnings("unused")
    private static final String TAG = "GLWallpaperService";

    class GLWallpaperEngine extends Engine {
        private static final String TAG = "GLWallpaperEngine";
        private final Context context;
        private GLWallpaperSurfaceView glSurfaceView = null;
        private SimpleExoPlayer exoPlayer = null;
        private MediaSource videoSource = null;
        private DefaultTrackSelector trackSelector = null;
        private WallpaperCard wallpaperCard = null;
        private WallpaperCard oldWallpaperCard = null;
        private GLWallpaperRenderer renderer = null;
        private boolean allowSlide = false;
        private int videoRotation = 0;
        private int videoWidth = 0;
        private int videoHeight = 0;
        private long progress = 0;

        private class GLWallpaperSurfaceView extends GLSurfaceView {
            @SuppressWarnings("unused")
            private static final String TAG = "GLWallpaperSurface";

            public GLWallpaperSurfaceView(Context context) {
                super(context);
            }

            /**
             * This is a hack. Because Android Live Wallpaper only has a Surface.
             * So we create a GLSurfaceView, and when drawing to its Surface,
             * we replace it with WallpaperEngine's Surface.
             */
            @Override
            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }

            void onDestroy() {
                super.onDetachedFromWindow();
            }
        }

        GLWallpaperEngine(@NonNull final Context context) {
            this.context = context;
            setTouchEventsEnabled(false);
        }

        // @Override
        // public void onTouchEvent(MotionEvent event) {
        //     super.onTouchEvent(event);
        // }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            allowSlide = false;
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
            createGLSurfaceView();
            int width = surfaceHolder.getSurfaceFrame().width();
            int height = surfaceHolder.getSurfaceFrame().height();
            renderer.setScreenSize(width, height);
            startPlayer();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (renderer != null) {
                if (visible) {
                    allowSlide = false;
                    glSurfaceView.onResume();
                    startPlayer();
                } else {
                    stopPlayer();
                    glSurfaceView.onPause();
                    // Prevent useless renderer calculating.
                    allowSlide = false;
                }
            }
        }

        @Override
        public void onOffsetsChanged(
            float xOffset, float yOffset,
            float xOffsetStep, float yOffsetStep,
            int xPixelOffset, int yPixelOffset
        ) {
            super.onOffsetsChanged(
                xOffset, yOffset, xOffsetStep,
                yOffsetStep, xPixelOffset, yPixelOffset
            );
            if (allowSlide && !isPreview()) {
                renderer.setOffset(0.5f - xOffset, 0.5f - yOffset);
            }
        }

        @Override
        public void onSurfaceChanged(
            SurfaceHolder surfaceHolder, int format,
            int width, int height
        ) {
            super.onSurfaceChanged(surfaceHolder, format, width, height);
            renderer.setScreenSize(width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            stopPlayer();
            glSurfaceView.onDestroy();
        }

        private void createGLSurfaceView() {
            if (glSurfaceView != null) {
                glSurfaceView.onDestroy();
                glSurfaceView = null;
            }
            glSurfaceView = new GLWallpaperSurfaceView(context);
            final ActivityManager activityManager = (ActivityManager)getSystemService(
                Context.ACTIVITY_SERVICE
            );
            if (activityManager == null) {
                throw new RuntimeException("Cannot get ActivityManager");
            }
            final ConfigurationInfo configInfo = activityManager.getDeviceConfigurationInfo();
            if (configInfo.reqGlEsVersion >= 0x30000) {
                glSurfaceView.setEGLContextClientVersion(3);
                renderer = new GLES30WallpaperRenderer(context);
            } else if (configInfo.reqGlEsVersion >= 0x20000) {
                glSurfaceView.setEGLContextClientVersion(2);
                renderer = new GLES20WallpaperRenderer(context);
            } else {
                Toast.makeText(context, R.string.gles_version, Toast.LENGTH_LONG).show();
                throw new RuntimeException("Needs GLESv2 or higher");
            }
            glSurfaceView.setPreserveEGLContextOnPause(true);
            glSurfaceView.setRenderer(renderer);
            // On demand render will lead to black screen.
            glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        }

        private boolean checkWallpaperCardValid() {
//            if (wallpaperCard == null) {
//                return false;
//            }
//            if (wallpaperCard.getType() == WallpaperCard.Type.INTERNAL) {
//                return true;
//            }
//            boolean res = true;
//            // Ask persistable permission here because AddCardTask may not have context.
//            getContentResolver().takePersistableUriPermission(
//                wallpaperCard.getUri(), Intent.FLAG_GRANT_READ_URI_PERMISSION
//            );
//            try {
//                final ContentResolver resolver = getContentResolver();
//                final ParcelFileDescriptor pfd = resolver.openFileDescriptor(
//                    wallpaperCard.getUri(), "r"
//                );
//                if (pfd == null) {
//                    res = false;
//                } else {
//                    pfd.close();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                res = false;
//            } catch (IOException e) {
//                e.printStackTrace();
//                res = false;
//            }
//            return res;
            return true;
        }

        private void loadWallpaperCard() {
            oldWallpaperCard = wallpaperCard;
            if (isPreview()) {
                wallpaperCard = WeatherApplication.getPreviewWallpaperCard();
            } else {
                wallpaperCard = WeatherApplication.getCurrentWallpaperCard(context);
            }
            if (!checkWallpaperCardValid()) {
                if (wallpaperCard != null) {
                    // File is removed by user.
                    Toast.makeText(context, R.string.invalid_path, Toast.LENGTH_LONG).show();
                    wallpaperCard.setInvalid();
                }
                // Load default wallpaper.
                final List<WallpaperCard> cards = WeatherApplication.getCards(context);
                if (cards.size() > 0 && cards.get(0) != null) {
                    wallpaperCard = cards.get(0);
                } else {
                    wallpaperCard = null;
                    Toast.makeText(context, R.string.default_failed, Toast.LENGTH_LONG).show();
                    throw new RuntimeException("Failed to fallback to internal wallpaper");
                }
            }
        }

        private void getVideoMetadata() throws IOException {
            final MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            switch (wallpaperCard.getType()) {
            case INTERNAL:
                final AssetFileDescriptor afd = getAssets().openFd(wallpaperCard.getPath());
                mmr.setDataSource(
                    afd.getFileDescriptor(),
                    afd.getStartOffset(),
                    afd.getDeclaredLength()
                );
                afd.close();
                break;
            case EXTERNAL:
                mmr.setDataSource(context, wallpaperCard.getUri());
                break;
            }
            final String rotation = mmr.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION
            );
            final String width = mmr.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH
            );
            final String height = mmr.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT
            );
            mmr.release();
            videoRotation = Integer.parseInt(rotation);
            videoWidth = Integer.parseInt(width);
            videoHeight = Integer.parseInt(height);
        }

        private void startPlayer() {
            if (exoPlayer != null) {
                stopPlayer();
            }
            Utils.debug(TAG, "Player starting");
            loadWallpaperCard();
            if (wallpaperCard == null) {
                // gg
                return;
            }
            try {
                getVideoMetadata();
            } catch (IOException e) {
                e.printStackTrace();
                // gg
                return;
            }
            trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            exoPlayer.setVolume(0.0f);
            // Disable audio decoder.
            final int count = exoPlayer.getRendererCount();
            for (int i = 0; i < count; ++i) {
                if (exoPlayer.getRendererType(i) == C.TRACK_TYPE_AUDIO) {
                    trackSelector.setParameters(
                        trackSelector.buildUponParameters().setRendererDisabled(i, true)
                    );
                }
            }
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
            final DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "com.allfree.wallpaperparallax.wallpaper3d.wallpaperhd.wallpaper4k.livewallpaper.dev")
            );
            // ExoPlayer can load file:///android_asset/ uri correctly.
            videoSource = new ExtractorMediaSource.Factory(
                dataSourceFactory
            ).createMediaSource(wallpaperCard.getUri());
            // Let we assume video has correct info in metadata, or user should fix it.
            renderer.setVideoSizeAndRotation(videoWidth, videoHeight, videoRotation);
            // This must be set after getting video info.
            renderer.setSourcePlayer(exoPlayer);
            exoPlayer.prepare(videoSource);
            // ExoPlayer's video size changed listener is buggy. Don't use it.
            // It give's width and height after rotation, but did not rotate frames.
            if (oldWallpaperCard != null &&
                oldWallpaperCard.equals(wallpaperCard)) {
                exoPlayer.seekTo(progress);
            }
            exoPlayer.setPlayWhenReady(true);
        }

        private void stopPlayer() {
            if (exoPlayer != null) {
                if (exoPlayer.getPlayWhenReady()) {
                    Utils.debug(TAG, "Player stopping");
                    exoPlayer.setPlayWhenReady(false);
                    progress = exoPlayer.getCurrentPosition();
                    exoPlayer.stop();
                }
                exoPlayer.release();
                exoPlayer = null;
            }
            videoSource = null;
            trackSelector = null;
        }
    }

    @Override
    public Engine onCreateEngine() {
        return new GLWallpaperEngine(this);
    }
}
