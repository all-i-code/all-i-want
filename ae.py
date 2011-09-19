'''
#
# File: ae.py
# Description: wrapper for app engine services
# 
# Copyright 2011 Adam Meadows 
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#
'''

class Wrapper:

    FROM_ADDRESS = 'All I Want Mail <mail.all.i.want@gmail.com>'
    APPROVE_TEMPLATE = '''
Dear %s,

Your All I Want account has been activated. You can now visit
http://all-i-want.appspot.com/ and sign in using your Google Account
to access All I Want.

Please let us know if you have any questions.

Sincerely,

All I Want Mail
'''
    
    DENY_TEMPLATE = '''
Dear %s,

We're sorry to inform you that your All I Want account cannot be activated 
at this time. We are currently in a beta state and can only support limited
active accounts.

When more accounts can be supported, you will be notified at this address.
We're sorry that we cannot accomodate you at this time.

Sincerely,

All I Want Mail
'''
  
    def create_login_url(self, url):
        from google.appengine.api import users
        return users.create_login_url(url)

    def create_logout_url(self, url):
        from google.appengine.api import users
        return users.create_logout_url(url)

    def send_mail(self, to, subject, body):
        self.send_mail_from(self.FROM_ADDRESS, to, subject, body)

    def send_mail_from(self, sender, to, subject, body):
        from google.appengine.api import mail
        msg = mail.EmailMessage()
        msg.sender = sender
        msg.to = to
        msg.subject = subject
        msg.body = body
        msg.send()

