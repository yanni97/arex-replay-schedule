package com.arextest.schedule.mdc;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;

/**
 * @author jmo
 * @since 2021/11/5
 */
@Deprecated
@Slf4j
public abstract class AbstractTracedRunnable implements Runnable {

  private final Map<String, String> traceMap;

  public AbstractTracedRunnable() {
    this.traceMap = MDC.getCopyOfContextMap();
  }

  @Override
  public final void run() {
    Map<String, String> old = mark();
    try {
      this.doWithTracedRunning();
    } catch (Throwable ex) {
      LOGGER.error(ex.getMessage(), ex);
    } finally {
      removeMark(old);
    }
  }

  protected abstract void doWithTracedRunning();

  private Map<String, String> mark() {
    if (MapUtils.isEmpty(this.traceMap)) {
      return null;
    } else {
      Map<String, String> old = MDC.getCopyOfContextMap();
      MDC.setContextMap(this.traceMap);
      return old;
    }
  }

  private void removeMark(Map<String, String> prev) {
    if (MapUtils.isEmpty(prev)) {
      MDC.clear();
    } else {
      MDC.setContextMap(prev);
    }
  }
}