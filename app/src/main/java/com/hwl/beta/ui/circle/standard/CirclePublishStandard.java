package com.hwl.beta.ui.circle.standard;

import java.util.List;

import io.reactivex.Observable;

public interface CirclePublishStandard {
    Observable publishInfo(String content, List<String> imagePaths);
}
