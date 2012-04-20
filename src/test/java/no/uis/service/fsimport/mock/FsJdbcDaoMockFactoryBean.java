package no.uis.service.fsimport.mock;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import no.uis.service.client.jdbc.dao.FsJdbcDAO;

import org.easymock.EasyMock;
import org.springframework.beans.factory.FactoryBean;

public class FsJdbcDaoMockFactoryBean implements FactoryBean {

	private List<String> studyProgramIds;
	
	public void setStudyProgramIds(List<String> studyProgramIds) {
		this.studyProgramIds = studyProgramIds;
	}

	@Override
	public Object getObject() throws Exception {
		FsJdbcDAO fsJdbcDao = EasyMock.createMock(FsJdbcDAO.class);
		
		expect(fsJdbcDao.getPoliceCertRequiredStudyPrograms()).andReturn(studyProgramIds).atLeastOnce();
		replay(fsJdbcDao);
		
		return fsJdbcDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getObjectType() {
		return FsJdbcDAO.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
