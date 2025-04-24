package com.example.dishpatch.infra.db.admin.entity;

import com.example.dishpatch.infra.db.common.StatConvertible;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "admin_store_order_stats_monthly")
public class AdminStoreOrderStatMonthly extends AdminStoreStat implements StatConvertible {

}
