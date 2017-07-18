#!/usr/local/bin/python


# azure-deploy.py
# Jacob Zarobsky
# Deploys an application to Azure using FTPS.


import argparse, getpass, os, ssl
from ftplib import FTP_TLS

xdt_path = '/site'
files_path = '/site/wwwroot'

def parse_args():
    parser = argparse.ArgumentParser(description='Deploy applications and files to Azure')
    parser.add_argument('-d', '--deploy-dir', type=str, help='The directory with contents to deploy', required=True)
    parser.add_argument('--host', type=str, help='FTPS Host', required=True)
    parser.add_argument('-u', '--user', type=str, help='FTPS Username', required=True)
    parser.add_argument('-xdt', '--xdt-transform', type=str,
                        help='XDT Transformation to apply to applicationHost.config', default=None)
    parser.add_argument('--debug-level', type=int, help='Debug level of FTP service', default=0)
    return parser.parse_args()

def copy_files(args):
    password = getpass.getpass()
    file = None
    with FTP_TLS(host=args.host, user=args.user, passwd=password) as ftp:
        ftp.set_debuglevel(args.debug_level)
        if args.xdt_transform is not None:
            ftp.cwd(xdt_path)
            file = open(args.xdt_transform, 'rb')
            ftp.storbinary('STOR applicationHost.xdt', file)
        ftp.cwd(files_path)
        place_files(ftp, args.deploy_dir)
        ftp.quit()

# https://stackoverflow.com/questions/32481640/how-do-i-upload-full-directory-on-ftp-in-python
def place_files(ftp, deploy_path):
    for name in os.listdir(deploy_path):
        local_path = os.path.join(deploy_path, name)
        if os.path.isfile(local_path):
            ftp.storbinary('STOR ' + name, open(local_path, 'rb'))
        else:
            try:
                ftp.mkd(name)
            except:
                pass
            ftp.cwd(name)
            place_files(ftp, local_path)
            ftp.cwd("..")

if __name__ == '__main__':
    copy_files(parse_args())
