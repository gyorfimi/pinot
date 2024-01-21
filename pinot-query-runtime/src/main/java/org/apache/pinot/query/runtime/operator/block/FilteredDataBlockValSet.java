/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pinot.query.runtime.operator.block;

import java.math.BigDecimal;
import javax.annotation.Nullable;
import org.apache.pinot.common.datablock.DataBlock;
import org.apache.pinot.common.datablock.DataBlockUtils;
import org.apache.pinot.common.utils.DataSchema;
import org.apache.pinot.segment.spi.index.reader.Dictionary;
import org.apache.pinot.spi.data.FieldSpec;
import org.roaringbitmap.RoaringBitmap;


/**
 * In the multistage engine, the leaf stage servers process the data in columnar fashion. By the time the
 * intermediate stage receives the projected column, they are converted to a row based format. This class provides
 * the capability to convert the row based representation into columnar blocks so that they can be used to process
 * aggregations using v1 aggregation functions.
 * TODO: Support MV
 */
public class FilteredDataBlockValSet extends DataBlockValSet {
  private final int _filterIdx;

  public FilteredDataBlockValSet(DataSchema.ColumnDataType columnDataType, DataBlock dataBlock, int colIndex,
      int filterIdx) {
    super(columnDataType, dataBlock, colIndex);
    _filterIdx = filterIdx;
  }

  /**
   * Returns a bitmap of indices where null values are found.
   */
  @Nullable
  @Override
  public RoaringBitmap getNullBitmap() {
    return _nullBitMap;
  }

  @Override
  public FieldSpec.DataType getValueType() {
    return _dataType;
  }

  @Override
  public boolean isSingleValue() {
    // TODO: Needs to be changed when we start supporting MV in multistage
    return true;
  }

  @Nullable
  @Override
  public Dictionary getDictionary() {
    return null;
  }

  @Override
  public int[] getDictionaryIdsSV() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int[] getIntValuesSV() {
    return DataBlockUtils.extractIntValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public long[] getLongValuesSV() {
    return DataBlockUtils.extractLongValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public float[] getFloatValuesSV() {
    return DataBlockUtils.extractFloatValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public double[] getDoubleValuesSV() {
    return DataBlockUtils.extractDoubleValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public BigDecimal[] getBigDecimalValuesSV() {
    return DataBlockUtils.extractBigDecimalValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public String[] getStringValuesSV() {
    return DataBlockUtils.extractStringValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public byte[][] getBytesValuesSV() {
    return DataBlockUtils.extractBytesValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public int[][] getDictionaryIdsMV() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int[][] getIntValuesMV() {
    return DataBlockUtils.extractIntMultiValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public long[][] getLongValuesMV() {
    return DataBlockUtils.extractLongMultiValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public float[][] getFloatValuesMV() {
    return DataBlockUtils.extractFloatMultiValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public double[][] getDoubleValuesMV() {
    return DataBlockUtils.extractDoubleMultiValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public String[][] getStringValuesMV() {
    return DataBlockUtils.extractStringMultiValuesForColumn(_dataBlock, _index, _filterIdx);
  }

  @Override
  public byte[][][] getBytesValuesMV() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int[] getNumMVEntries() {
    return DataBlockUtils.extractNumMVEntriesForColumn(_dataBlock, _index, _filterIdx);
  }
}
