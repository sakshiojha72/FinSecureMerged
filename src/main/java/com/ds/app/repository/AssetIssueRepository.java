package com.ds.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ds.app.entity.Asset;
import com.ds.app.entity.AssetIssue;
import com.ds.app.entity.Employee;
import com.ds.app.enums.AssetIssueStatus;

@Repository
public interface AssetIssueRepository extends JpaRepository<AssetIssue, Long>{

	List<AssetIssue> findByEmployee_UserId(Integer userId);	List<AssetIssue> findByStatus(AssetIssueStatus status);
	List<AssetIssue> findByAsset_AssetId(Long assetId);
	boolean existsByAssetAndEmployeeAndStatus(Asset asset, Employee employee, AssetIssueStatus open);
}
