En windows
vagrant init 

Vagrantfile
```
Vagrant.configure("2") do |config|
  # Define the base box
  config.vm.box = "ubuntu/bionic64"

  # Provision using Ansible
  config.vm.provision "ansible" do |ansible|
    # Path to your playbook
    ansible.playbook = "playbook.yml"

    # Ensure the roles directory is included
    ansible.galaxy_role_file = "requirements.yml"
    ansible.galaxy_roles_path = "roles"
  end
  # Sync the roles folder (optional, but useful)
  config.vm.synced_folder "./roles", "/vagrant/roles"
end
```


Crear playbook.yml
```yml
---
- hosts: all
  become: true
  roles:
    - geerlingguy.gitlab
```
en linux wsl
 ansible-galaxy role install geerlingguy.gitlab

Crear archivo:
```yml
---
- src: geerlingguy.gitlab
```

en linux wsl:
ansible-galaxy install -r requirements.yml -p roles

