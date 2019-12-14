package nil.ed.chatroom.util.nlp.seg.impl;

import nil.ed.chatroom.util.nlp.seg.SegmentAdapter;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;

import java.util.List;

/**
 * @author delin10
 * @since 2019/10/24
 **/
public class WordSegmentAdapter implements SegmentAdapter {
    @Override
    public List<String> seg(String text) {
         WordSegmenter.seg(text, SegmentationAlgorithm.FullSegmentation);
         return null;
    }
}
