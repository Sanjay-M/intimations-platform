package com.codingkapoor.passwordless.impl.core

import com.codingkapoor.common.Mailer
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents
import com.codingkapoor.employee.api.EmployeeService
import com.codingkapoor.passwordless.api.PasswordlessService
import com.codingkapoor.passwordless.impl.repositories.employee.EmployeeDao
import com.codingkapoor.passwordless.impl.repositories.otp.OTPDao
import com.codingkapoor.passwordless.impl.repositories.token.RefreshTokenDao
import com.codingkapoor.passwordless.impl.services.{OTPMailer, PasswordlessServiceImpl}
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.persistence.slick.SlickPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import play.api.db.HikariCPComponents

abstract class PasswordlessApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with SlickPersistenceComponents
    with HikariCPComponents
    with LagomKafkaClientComponents
    with AhcWSComponents {
  override lazy val lagomServer: LagomServer = serverFor[PasswordlessService](wire[PasswordlessServiceImpl])
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = PasswordlessSerializerRegistry

  lazy val employeeService: EmployeeService = serviceClient.implement[EmployeeService]

  lazy val otpMailer: OTPMailer = wire[OTPMailer]
  lazy val mailer: Mailer = wire[Mailer]

  lazy val otpDao: OTPDao = wire[OTPDao]
  lazy val refreshTokenDao: RefreshTokenDao = wire[RefreshTokenDao]
  lazy val employeeDao: EmployeeDao = wire[EmployeeDao]
}
