/*
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.videodemo.videodemo.media;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;

import com.demo.videodemo.videodemo.R;

public class AndroidMediaController extends FrameLayout implements IMediaController {
    private Context mContext;
    private LayoutInflater inflater;
    private View viewRoot;
    private boolean isShowing = false;

    private final int CODE_SHOW_LOAD = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_SHOW_LOAD:
                    hide();
                    break;
                default:
                    break;
            }
        }
    };


    public AndroidMediaController(Context context) {
        super(context);
        init(context);

    }

    public AndroidMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AndroidMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewRoot = inflater.inflate(R.layout.zp_on_loading_player_layout, this);
    }

    public void show() {
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
            if (viewRoot.getVisibility() != VISIBLE) {
                viewRoot.setVisibility(VISIBLE);
            }
        }
        isShowing = true;
    }

    @Override
    public void showOnce(View view) {

    }

    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {

    }

    /**
     * 显示一段时间后消失
     * @param timeout 显示时间,如果为0就一直显示
     */
    public void show(int timeout) {//只负责上下两条bar的显示,不负责中央loading,error,playBtn的显示.
        if (timeout < 0)
            return;

        show();

        if (timeout != 0) {
            mHandler.sendEmptyMessageDelayed(CODE_SHOW_LOAD, timeout);
        }
    }

    public void hide() {
        Log.e("", "visible = " + viewRoot.getVisibility());
        if (viewRoot.getVisibility() == VISIBLE) {
            viewRoot.setVisibility(GONE);
        }

        isShowing = false;
    }
}
