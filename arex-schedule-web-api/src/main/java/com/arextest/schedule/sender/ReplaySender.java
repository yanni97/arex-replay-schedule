package com.arextest.schedule.sender;

import com.arextest.schedule.model.ReplayActionCaseItem;
import java.util.Map;

/**
 * @author jmo
 * @since 2021/9/16
 */
public interface ReplaySender {

  /**
   * Indicate the instance should be working for the message content type, return true should be
   * used,others skipped
   */
  boolean isSupported(String categoryType);

  /**
   * Try to send the replay case to remote target host
   */
  boolean send(ReplayActionCaseItem caseItem);

  default boolean send(ReplayActionCaseItem caseItem, Map<String, String> extraHeaders) {
    return send(caseItem);
  }

  /**
   * The value is optional and represents an order value. Higher values have higher priority. The
   * default value is 0. For the same category type sender if you want to implement it, make sure
   * that the order is greater than default implement.
   *
   * @return order
   */
  default int getOrder() {
    return 0;
  }
}