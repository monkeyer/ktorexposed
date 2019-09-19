package fan.zheyuan.ktorexposed.domain.repository

import com.datastax.oss.driver.api.core.CqlSession

fun cassandraSession(): CqlSession = CqlSession.builder().build()