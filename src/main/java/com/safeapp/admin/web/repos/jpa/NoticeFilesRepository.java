package com.safeapp.admin.web.repos.jpa;

import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.AccidentExpFiles;
import com.safeapp.admin.web.model.entity.Notice;
import com.safeapp.admin.web.model.entity.NoticeFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeFilesRepository extends JpaRepository<NoticeFiles, Long> {

    List<NoticeFiles> findByNotice(Notice notice);

}