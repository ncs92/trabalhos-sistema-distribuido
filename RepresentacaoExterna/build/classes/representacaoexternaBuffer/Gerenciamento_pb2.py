# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: representacaoexternaBuffer/Gerenciamento.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='representacaoexternaBuffer/Gerenciamento.proto',
  package='representacaoexternaBuffer',
  syntax='proto2',
  serialized_options=None,
  serialized_pb=_b('\n.representacaoexternaBuffer/Gerenciamento.proto\x12\x1arepresentacaoexternaBuffer\"\xd6\x01\n\rGerenciamento\x12\x0e\n\x06titulo\x18\x01 \x02(\t\x12\r\n\x05\x61utor\x18\x02 \x02(\t\x12\x0e\n\x06\x63\x61ntor\x18\x03 \x02(\t\x12\x0b\n\x03\x61no\x18\x04 \x02(\x05\x12\x12\n\nnum_pagina\x18\x05 \x02(\x05\x12\x0c\n\x04isbn\x18\x06 \x02(\t\x12\x0c\n\x04peso\x18\x07 \x02(\x02\x12\x31\n\x03\x64im\x18\x08 \x02(\x0b\x32$.representacaoexternaBuffer.Dimensao\x12\r\n\x05\x61lbum\x18\t \x02(\t\x12\x17\n\x0f\x64\x61ta_lancamento\x18\n \x02(\t\"@\n\x08\x44imensao\x12\x0e\n\x06\x61ltura\x18\x01 \x02(\x02\x12\x0f\n\x07largura\x18\x02 \x02(\x02\x12\x13\n\x0b\x63omprimento\x18\x03 \x02(\x02')
)




_GERENCIAMENTO = _descriptor.Descriptor(
  name='Gerenciamento',
  full_name='representacaoexternaBuffer.Gerenciamento',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='titulo', full_name='representacaoexternaBuffer.Gerenciamento.titulo', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='autor', full_name='representacaoexternaBuffer.Gerenciamento.autor', index=1,
      number=2, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='cantor', full_name='representacaoexternaBuffer.Gerenciamento.cantor', index=2,
      number=3, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='ano', full_name='representacaoexternaBuffer.Gerenciamento.ano', index=3,
      number=4, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='num_pagina', full_name='representacaoexternaBuffer.Gerenciamento.num_pagina', index=4,
      number=5, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='isbn', full_name='representacaoexternaBuffer.Gerenciamento.isbn', index=5,
      number=6, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='peso', full_name='representacaoexternaBuffer.Gerenciamento.peso', index=6,
      number=7, type=2, cpp_type=6, label=2,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='dim', full_name='representacaoexternaBuffer.Gerenciamento.dim', index=7,
      number=8, type=11, cpp_type=10, label=2,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='album', full_name='representacaoexternaBuffer.Gerenciamento.album', index=8,
      number=9, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='data_lancamento', full_name='representacaoexternaBuffer.Gerenciamento.data_lancamento', index=9,
      number=10, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=79,
  serialized_end=293,
)


_DIMENSAO = _descriptor.Descriptor(
  name='Dimensao',
  full_name='representacaoexternaBuffer.Dimensao',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='altura', full_name='representacaoexternaBuffer.Dimensao.altura', index=0,
      number=1, type=2, cpp_type=6, label=2,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='largura', full_name='representacaoexternaBuffer.Dimensao.largura', index=1,
      number=2, type=2, cpp_type=6, label=2,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='comprimento', full_name='representacaoexternaBuffer.Dimensao.comprimento', index=2,
      number=3, type=2, cpp_type=6, label=2,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=295,
  serialized_end=359,
)

_GERENCIAMENTO.fields_by_name['dim'].message_type = _DIMENSAO
DESCRIPTOR.message_types_by_name['Gerenciamento'] = _GERENCIAMENTO
DESCRIPTOR.message_types_by_name['Dimensao'] = _DIMENSAO
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

Gerenciamento = _reflection.GeneratedProtocolMessageType('Gerenciamento', (_message.Message,), dict(
  DESCRIPTOR = _GERENCIAMENTO,
  __module__ = 'representacaoexternaBuffer.Gerenciamento_pb2'
  # @@protoc_insertion_point(class_scope:representacaoexternaBuffer.Gerenciamento)
  ))
_sym_db.RegisterMessage(Gerenciamento)

Dimensao = _reflection.GeneratedProtocolMessageType('Dimensao', (_message.Message,), dict(
  DESCRIPTOR = _DIMENSAO,
  __module__ = 'representacaoexternaBuffer.Gerenciamento_pb2'
  # @@protoc_insertion_point(class_scope:representacaoexternaBuffer.Dimensao)
  ))
_sym_db.RegisterMessage(Dimensao)


# @@protoc_insertion_point(module_scope)
