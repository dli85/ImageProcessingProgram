import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import imageprocessing.view.HistogramPanel;
import imageprocessing.view.HistogramPanelImpl;

import static org.junit.Assert.assertEquals;

/**
 * Tests for our histogram. Most of our histogram functionality is kept hidden, so accessing
 * histogram information is restricted. Our testing will be done visually, and the tests in the
 * below class ensure that the methods run without throwing errors.
 */
public class HistogramPanelTest {
  HistogramPanel histo;

  @Before
  public void testConstructor() {
    histo = new HistogramPanelImpl(10, 10);
  }

  @Test
  public void testAddColorDataAndReset() {
    Map<Integer, Integer> reds = new HashMap<Integer, Integer>();
    Map<Integer, Integer> greens = new HashMap<Integer, Integer>();
    Map<Integer, Integer> blues = new HashMap<Integer, Integer>();
    Map<Integer, Integer> intensities = new HashMap<Integer, Integer>();
    for (int i = 0; i < 10; i++) {
      reds.put(i, i);
      greens.put(i, i);
      blues.put(i, i);
      intensities.put(i, i);
    }

    this.histo.addColorData(reds, greens, blues, intensities);
    assertEquals(0, (int) reds.get(0));

    this.histo.reset();
  }
}
