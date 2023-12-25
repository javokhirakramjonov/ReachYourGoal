package me.javahere.reachyourgoal.exception

class ReachYourGoalException(val exceptionType: ReachYourGoalExceptionType) : RuntimeException(exceptionType.message)