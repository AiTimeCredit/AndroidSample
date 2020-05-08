package com.android.common.mvvm;

/**
 * Model抽象接口：负责业务逻辑和实体模型(主要职责是存储、检索、操纵数据)
 * <ul>
 * <li>对外提供业务数据API
 * <li>内部实现本地数据, 网络数据的存取等
 * <li>只有ViewModel可以访问，与View隔离
 * </ul>
 */
public interface IModel {

}
