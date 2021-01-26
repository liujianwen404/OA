package com.ruoyi.hr.manager;

public interface IHrDomainConvert<T,S> {
    S convert(T t);
}
