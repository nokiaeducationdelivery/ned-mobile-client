package org.ned.client;

import java.util.Vector;

/**
 *
 * @author damian.janicki
 */
public interface IContent {
    String getMediaFile();
    String getText();
    String getParentId();
    String getType();
    String getId();
    String getDescription();
    Vector getExternalLinks();
}
