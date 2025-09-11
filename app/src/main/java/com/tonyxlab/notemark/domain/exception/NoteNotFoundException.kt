package com.tonyxlab.notemark.domain.exception

class NoteNotFoundException (id: Long): Exception("Note with Id $id not Found")
class AuthException(msg: String) : Exception(msg)