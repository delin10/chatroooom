package nil.ed.chatroom.util.sensitive.seg.impl;

import nil.ed.chatroom.util.sensitive.seg.Segmenter;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author delin10
 * @since 2019/10/24
 **/
public class WordSegmenterImpl implements Segmenter {
    @Override
    public List<String> seg(String text) {
         return org.apdplat.word.WordSegmenter.segWithStopWords(text, SegmentationAlgorithm.FullSegmentation).stream()
                 .map(Word::getText).collect(Collectors.toList());
    }
}
