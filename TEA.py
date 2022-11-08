#!/usr/bin/env python
# coding: utf-8

# ## Tiny Encryption Algorithm
# resources used:
# https://en.wikipedia.org/wiki/Tiny_Encryption_Algorithm
# https://gist.github.com/twheys/4e83567942172f8ba85058fae6bfeef5

# In[46]:


import base64
import ctypes
import itertools
import math


# In[81]:


#just call the encrypt and decrypt functions as needed i linked all the resources i uesd while implementing the TEA function


# In[69]:


def _chunks(iterable, n):
    it = iter(iterable)
    while True:
        chunk = tuple(itertools.islice(it, n))
        if not chunk:
            return
        yield chunk


# In[70]:


def _str2vec(value):
    l=4
    n = len(value)
    num = math.ceil(n / l)
    chunks = [value[l * i:l * (i + 1)]
              for i in range(num)]

    return [sum([character << 8 * j
                 for j, character in enumerate(chunk)])
            for chunk in chunks]


# In[71]:


def _vec2str(vector):
    l=4
    return bytes((element >> 8 * i) & 0xff
                 for element in vector
                 for i in range(l)).replace(b'\x00', b'')


# In[72]:


def _encipher(m, k):
    y, z = [ctypes.c_uint32(x)
            for x in m]
    sum = ctypes.c_uint32(0)
    delta = 0x9E3779B9

    for n in range(32, 0, -1):
        sum.value += delta
        y.value += (z.value << 4) + k[0] ^ z.value + sum.value ^ (z.value >> 5) + k[1]
        z.value += (y.value << 4) + k[2] ^ y.value + sum.value ^ (y.value >> 5) + k[3]

    return [y.value, z.value]


# In[86]:


def _decipher(m, k):
    y, z = [ctypes.c_uint32(x)
            for x in m]
    sum = ctypes.c_uint32(0xC6EF3720)
    delta = 0x9E3779B9

    for n in range(32, 0, -1):
        z.value -= (y.value << 4) + k[2] ^ y.value + sum.value ^ (y.value >> 5) + k[3]
        y.value -= (z.value << 4) + k[0] ^ z.value + sum.value ^ (z.value >> 5) + k[1]
        sum.value -= delta

    return [y.value, z.value]


# In[87]:


def encrypt(plaintext, key):
    if not plaintext:
        return ''

    m = _str2vec(plaintext.encode())
    k = _str2vec(key.encode()[:16])

    bytearray = b''.join(_vec2str(_encipher(chunk, k))
                         for chunk in _chunks(m, 2))

    return base64.b64encode(bytearray).decode()


# In[88]:


def decrypt(ciphertext, key):
    if not ciphertext:
        return ''

    k = _str2vec(key.encode()[:16])
    m = _str2vec(base64.b64decode(ciphertext.encode()))

    return b''.join(_vec2str(_decipher(chunk, k))
                    for chunk in _chunks(m, 2)).decode()


# In[89]:


print(decrypt('3LJ2zCUv6Pg=','0123456789abcdef'))


# In[ ]:





# In[ ]:




