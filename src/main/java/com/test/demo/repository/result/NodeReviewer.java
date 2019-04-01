package com.test.demo.repository.result;

import com.test.demo.model.Node;
import com.test.demo.model.Reviewer;
import com.test.demo.model.Signoff;

public interface NodeReviewer {
    Node getNode();
    Signoff getSignoff();
}
