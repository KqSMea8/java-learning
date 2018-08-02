package melon.mybatis.mapper;

import melon.mybatis.po.Customer;
import tk.mybatis.mapper.common.Mapper;

/**
 * @user: melon.zhao
 * @date: 2018/7/31
 */
public interface CustomerMapper extends Mapper<Customer> {
    String selectCustomer(Customer customer);

}
