package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.ConcernAccidentExp;
import com.safeapp.admin.web.model.entity.ConcernAccidentExpFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcernAccidentExpFilesRepository extends JpaRepository<ConcernAccidentExpFiles, Long> {

    List<ConcernAccidentExpFiles> findByConExp(ConcernAccidentExp conExp);

}