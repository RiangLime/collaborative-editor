package cn.rianglime.collaborativeeditor.constant;

/**
 * @ClassName: OperationEnum
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/2/29 17:03
 */
public enum OperationEnum {
    INSERT(1),
    DELETE(2);
    private final int val;
    OperationEnum(int dbVal) {
        this.val = dbVal;
    }
    public int getVal() {
        return val;
    }
}
